/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CraTestModule } from '../../../test.module';
import { ConfValueComponent } from 'app/entities/conf-value/conf-value.component';
import { ConfValueService } from 'app/entities/conf-value/conf-value.service';
import { ConfValue } from 'app/shared/model/conf-value.model';

describe('Component Tests', () => {
    describe('ConfValue Management Component', () => {
        let comp: ConfValueComponent;
        let fixture: ComponentFixture<ConfValueComponent>;
        let service: ConfValueService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [ConfValueComponent],
                providers: []
            })
                .overrideTemplate(ConfValueComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ConfValueComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfValueService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ConfValue(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.confValues[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
