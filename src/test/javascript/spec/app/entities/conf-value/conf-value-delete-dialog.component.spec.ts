/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CraTestModule } from '../../../test.module';
import { ConfValueDeleteDialogComponent } from 'app/entities/conf-value/conf-value-delete-dialog.component';
import { ConfValueService } from 'app/entities/conf-value/conf-value.service';

describe('Component Tests', () => {
    describe('ConfValue Management Delete Component', () => {
        let comp: ConfValueDeleteDialogComponent;
        let fixture: ComponentFixture<ConfValueDeleteDialogComponent>;
        let service: ConfValueService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [ConfValueDeleteDialogComponent]
            })
                .overrideTemplate(ConfValueDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ConfValueDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfValueService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
