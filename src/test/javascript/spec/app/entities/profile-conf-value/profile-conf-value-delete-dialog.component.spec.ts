/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CraTestModule } from '../../../test.module';
import { ProfileConfValueDeleteDialogComponent } from 'app/entities/profile-conf-value/profile-conf-value-delete-dialog.component';
import { ProfileConfValueService } from 'app/entities/profile-conf-value/profile-conf-value.service';

describe('Component Tests', () => {
    describe('ProfileConfValue Management Delete Component', () => {
        let comp: ProfileConfValueDeleteDialogComponent;
        let fixture: ComponentFixture<ProfileConfValueDeleteDialogComponent>;
        let service: ProfileConfValueService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [ProfileConfValueDeleteDialogComponent]
            })
                .overrideTemplate(ProfileConfValueDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProfileConfValueDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfileConfValueService);
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
